import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <div className="page">
      <header className="topbar fade-in">
        <span className="logo-dot" aria-hidden />
        <span className="logo-text">Items</span>
        <nav className="topbar-links">
          <Link to="/login" className="link">
            Sign in
          </Link>
          <Link to="/register" className="btn small primary">
            Create account
          </Link>
        </nav>
      </header>

      <main className="layout fade-in stagger">
        <section className="card hero-card">
          <p className="eyebrow">Spring Boot • JWT • React • MySQL</p>
          <h1>A calm place to stash your lists</h1>
          <p className="muted lead">
            Register once, store everything in{' '}
            <code className="inline-code">spring_securitydb</code>, then sign back in anytime.
          </p>
          <div className="row">
            <Link to="/register" className="btn primary">
              Get started
            </Link>
            <Link to="/login" className="btn ghost">
              I already have an account
            </Link>
          </div>
        </section>

        <section className="card muted-card">
          <h2>For local development</h2>
          <ol className="steps">
            <li>
              Boot the API (<code>mvn spring-boot:run …</code> with DB credentials wired up).
            </li>
            <li>
              In another terminal run <code>cd frontend && npm install && npm run dev</code> — this UI proxies{' '}
              <code>/api</code>
              {' '}to{' '}
              <code>http://localhost:8080</code>.
            </li>
            <li>Open <code>http://localhost:5173</code> — you should land here.</li>
          </ol>
          <p className="muted small">
            For a packaged build, run <code>npm run build</code> so Vite emits static assets consumed by Spring Boot from{' '}
            <code className="inline-code">src/main/resources/static</code>.
          </p>
        </section>
      </main>
    </div>
  );
}
