import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
    try {
      await register({ username, email, password });
      navigate('/dashboard', { replace: true });
    } catch (err) {
      setError(err.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card fade-in">
        <p className="eyebrow">Step one</p>
        <h1>Create your account</h1>
        <p className="muted">We persist users in MySQL alongside your rows.</p>
        <form className="form" onSubmit={handleSubmit}>
          <label className="field">
            <span>Username</span>
            <input required minLength={3} value={username} onChange={(e) => setUsername(e.target.value)} />
          </label>
          <label className="field">
            <span>Email</span>
            <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
          </label>
          <label className="field">
            <span>Password</span>
            <input required type="password" minLength={8} value={password} onChange={(e) => setPassword(e.target.value)} />
            <small className="muted">At least 8 characters — kept hashed in MySQL.</small>
          </label>
          {error ? <div className="banner error">{error}</div> : null}
          <button className="btn primary fluid" disabled={loading} type="submit">
            {loading ? 'Saving…' : 'Register'}
          </button>
        </form>
        <p className="muted small">
          Already registered?{' '}
          <Link className="link" to="/login">
            Head to sign in.
          </Link>
        </p>
      </div>
    </div>
  );
}
