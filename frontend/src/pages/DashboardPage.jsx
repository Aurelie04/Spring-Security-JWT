import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { createItem, fetchItems } from '../api/items.js';

export default function DashboardPage() {
  const { token, username, logout } = useAuth();
  const [items, setItems] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [actionBusy, setActionBusy] = useState(false);
  const [banner, setBanner] = useState({ type: '', text: '' });

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        const data = await fetchItems(token);
        setItems(data);
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
                <li key={item.id}>
                  <p className="item-title">{item.name}</p>
                  {item.description ? <p className="muted item-desc">{item.description}</p> : null}
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  );
}
