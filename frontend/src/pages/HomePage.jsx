import { useEffect, useState } from 'react'
import { getHealth } from '../api/health'

const LABELS = {
  checking: 'Backend: checking...',
  up: 'Backend: UP',
  unreachable: 'Backend: unreachable',
}

export default function HomePage() {
  const [status, setStatus] = useState('checking')

  useEffect(() => {
    let active = true
    getHealth()
      .then((data) => {
        if (active) setStatus(data?.status === 'UP' ? 'up' : 'unreachable')
      })
      .catch(() => {
        if (active) setStatus('unreachable')
      })
    return () => {
      active = false
    }
  }, [])

  return (
    <main className="page">
      <h1>Library Management</h1>
      <p>Web application for managing a library catalog, members and loans.</p>
      <p className={`status status-${status}`}>{LABELS[status]}</p>
    </main>
  )
}
