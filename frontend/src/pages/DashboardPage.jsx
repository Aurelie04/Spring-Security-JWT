import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { createItem, deleteItem, fetchItems, updateItem } from '../api/items.js';

export default function DashboardPage() {
  const { token, username, logout } = useAuth();
  const [items, setItems] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [actionBusy, setActionBusy] = useState(false);
  const [banner, setBanner] = useState({ type: '', text: '' });

  const [editingId, setEditingId] = useState(null);
  const [editName, setEditName] = useState('');
  const [editDesc, setEditDesc] = useState('');

  async function reload() {
    const data = await fetchItems(token);
    setItems(data);
  }

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        await reload();
        setBanner({ type: '', text: '' });
      } catch (err) {
        setBanner({ type: 'error', text: err.message || 'Unable to load items.' });
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [token]);

  async function handleCreate(event) {
    event.preventDefault();
    try {
      setActionBusy(true);
      const saved = await createItem(token, { name, description });
      setItems((prev) => [...prev, saved]);
      setName('');
      setDescription('');
      setBanner({ type: 'success', text: 'Row saved straight to MySQL.' });
    } catch (err) {
      setBanner({ type: 'error', text: err.message });
    } finally {
      setActionBusy(false);
    }
  }

  function startEdit(item) {
    setEditingId(item.id);
    setEditName(item.name ?? '');
    setEditDesc(item.description ?? '');
    setBanner({ type: '', text: '' });
  }

  function cancelEdit() {
    setEditingId(null);
    setEditName('');
    setEditDesc('');
  }

  async function handleSaveEdit(event) {
    event.preventDefault();
    if (editingId == null) return;
    try {
      setActionBusy(true);
      const updated = await updateItem(token, editingId, { name: editName, description: editDesc });
      setItems((prev) => prev.map((row) => (row.id === updated.id ? updated : row)));
      cancelEdit();
      setBanner({ type: 'success', text: 'Item updated.' });
    } catch (err) {
      setBanner({ type: 'error', text: err.message });
    } finally {
      setActionBusy(false);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this item? This removes it from the database.')) return;
    try {
      setActionBusy(true);
      await deleteItem(token, id);
      setItems((prev) => prev.filter((row) => row.id !== id));
      if (editingId === id) cancelEdit();
      setBanner({ type: 'success', text: 'Item deleted.' });
    } catch (err) {
      setBanner({ type: 'error', text: err.message });
    } finally {
      setActionBusy(false);
    }
  }

  const empty = !loading && items.length === 0;

  return (
    <div className="dash-page fade-in">
      <header className="dash-toolbar">
        <div>
          <p className="muted small eyebrow-dash">Authenticated</p>
          <h2>Hello {username || 'collector'}</h2>
          <p className="muted">These entries are keyed to your user id in spring_securitydb.</p>
        </div>
        <div className="row">
          <Link className="btn ghost small" to="/">
            Back home
          </Link>
          <button type="button" className="btn secondary small" onClick={logout}>
            Log out
          </button>
        </div>
      </header>

      {banner.text ? (
        <div className={`banner ${banner.type === 'error' ? 'error' : 'success'} thin`}>{banner.text}</div>
      ) : null}

      <div className="dash-grid">
        <section className="card composer">
          <h3>Add an item</h3>
          <form className="form" onSubmit={handleCreate}>
            <label className="field">
              <span>Name</span>
              <input required value={name} onChange={(event) => setName(event.target.value)} />
            </label>
            <label className="field">
              <span>Notes</span>
              <textarea rows={4} value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Anything useful…" />
            </label>
            <button disabled={actionBusy || loading} className="btn primary fluid" type="submit">
              {actionBusy ? 'Saving…' : 'Save'}
            </button>
          </form>
        </section>

        <section className="card list-card">
          <div className="list-header">
            <h3>Your saved rows</h3>
            {loading ? <span className="pill">Refreshing…</span> : null}
          </div>
          {loading ? (
            <p className="muted">Talking to `/api/items`…</p>
          ) : empty ? (
            <p className="muted">Nothing captured yet — your first POST will populate the DB.</p>
          ) : (
            <ul className="item-feed">
              {items.map((item) => (
                <li key={item.id} className="item-row">
                  {editingId === item.id ? (
                    <form className="form item-edit-form" onSubmit={handleSaveEdit}>
                      <label className="field">
                        <span>Name</span>
                        <input required value={editName} onChange={(e) => setEditName(e.target.value)} />
                      </label>
                      <label className="field">
                        <span>Notes</span>
                        <textarea rows={3} value={editDesc} onChange={(e) => setEditDesc(e.target.value)} />
                      </label>
                      <div className="item-actions">
                        <button type="submit" disabled={actionBusy} className="btn primary small">
                          {actionBusy ? 'Saving…' : 'Save'}
                        </button>
                        <button type="button" disabled={actionBusy} className="btn ghost small" onClick={cancelEdit}>
                          Cancel
                        </button>
                      </div>
                    </form>
                  ) : (
                    <>
                      <div className="item-row-head">
                        <div>
                          <p className="item-title">{item.name}</p>
                          {item.description ? <p className="muted item-desc">{item.description}</p> : null}
                        </div>
                        <div className="item-actions">
                          <button type="button" className="btn ghost small" disabled={actionBusy} onClick={() => startEdit(item)}>
                            Edit
                          </button>
                          <button type="button" className="btn danger-outline small" disabled={actionBusy} onClick={() => handleDelete(item.id)}>
                            Delete
                          </button>
                        </div>
                      </div>
                    </>
                  )}
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  );
}
