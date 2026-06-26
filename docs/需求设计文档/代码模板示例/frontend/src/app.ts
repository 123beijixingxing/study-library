export function renderApp(root: HTMLElement | null): void {
  if (!root) {
    throw new Error('Missing #app root element')
  }

  root.innerHTML = `
    <main>
      <h1>Study Library Frontend Skeleton</h1>
      <p>Use the generated API and type files as the integration baseline.</p>
    </main>
  `
}
