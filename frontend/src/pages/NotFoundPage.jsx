import { Link } from 'react-router'

export default function NotFoundPage() {
  return (
    <main className="page">
      <h1>404 — Page not found</h1>
      <Link to="/">Back to home</Link>
    </main>
  )
}
