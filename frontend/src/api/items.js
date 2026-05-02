async function parseError(res) {
  try {
    const body = await res.json();
    return body.message || `${res.status} ${res.statusText}`;
  } catch {
    return `${res.status} ${res.statusText}`;
  }
}

export async function fetchItems(token) {
  const res = await fetch('/api/items', {
    headers: { Authorization: `Bearer ${token}`, Accept: 'application/json' },
  });
  if (!res.ok) {
    throw new Error(await parseError(res));
  }
  return res.json();
}

export async function createItem(token, item) {
  const res = await fetch('/api/items', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(item),
  });
  if (!res.ok) {
    throw new Error(await parseError(res));
  }
  return res.json();
}
