import { createContext, useCallback, useContext, useMemo, useState } from 'react';

const TOKEN_KEY = 'jwt';
const TOKEN_USER = 'jwt_user';

const AuthContext = createContext(null);

async function parseError(res) {
  try {
    const body = await res.json();
    if (body.fieldErrors && typeof body.fieldErrors === 'object') {
      const msgs = Object.entries(body.fieldErrors).map(([k, v]) => `${k}: ${v}`);
      return msgs.length ? msgs.join('; ') : body.message || `${res.status}`;
    }
    return body.message || `${res.status} ${res.statusText}`;
  } catch {
    return `${res.status} ${res.statusText}`;
  }
}

export function AuthProvider({ children }) {
  const [token, setTokenState] = useState(() => sessionStorage.getItem(TOKEN_KEY));
  const [username, setUsernameState] = useState(() => sessionStorage.getItem(TOKEN_USER));

  const logout = useCallback(() => {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(TOKEN_USER);
    setTokenState(null);
    setUsernameState(null);
  }, []);

  const persistSession = useCallback((jwt, userLabel) => {
    sessionStorage.setItem(TOKEN_KEY, jwt);
    sessionStorage.setItem(TOKEN_USER, userLabel ?? '');
    setTokenState(jwt);
    setUsernameState(userLabel ?? '');
  }, []);

  const register = useCallback(
    async (payload) => {
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        throw new Error(await parseError(res));
      }
      const data = await res.json();
      persistSession(data.token, data.username);
      return data;
    },
    [persistSession]
  );

  const login = useCallback(
    async (payload) => {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        throw new Error(await parseError(res));
      }
      const data = await res.json();
      persistSession(data.token, data.username);
      return data;
    },
    [persistSession]
  );

  const value = useMemo(
    () => ({ token, username, login, register, logout }),
    [token, username, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('AuthProvider missing');
  }
  return ctx;
}
