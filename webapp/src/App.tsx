import { useState } from 'react'
import './App.css'

export default function App() {
  const [userId, setUserId] = useState('')
  const [code, setCode] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleGetCode = async () => {
    if (!userId.trim()) {
      setError('Please enter a User ID')
      return
    }

    setLoading(true)
    setError('')
    setCode('')

    try {
      const response = await fetch(
        `/authenticator/code?userId=${encodeURIComponent(userId.trim())}`
      )

      if (!response.ok) {
        const text = await response.text()
        setError(text || 'Failed to fetch code')
        return
      }

      const result = await response.text()
      setCode(result)
    } catch {
      setError('Could not connect to the server')
    } finally {
      setLoading(false)
    }
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleGetCode()
    }
  }

  return (
    <div className="container">
      <div className="card">
        <h1 className="title">Authenticator</h1>
        <p className="subtitle">Generate your one-time code</p>

        <div className="form-group">
          <label htmlFor="userId" className="label">
            User ID
          </label>
          <input
            id="userId"
            type="text"
            className="input"
            placeholder="Enter your user ID"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            onKeyDown={handleKeyDown}
            disabled={loading}
          />
        </div>

        <button
          className="button"
          onClick={handleGetCode}
          disabled={loading}
        >
          {loading ? 'Fetching...' : 'Get Code'}
        </button>

        {code && (
          <div className="result">
            <p className="result-label">Your code</p>
            <p className="code">{code}</p>
          </div>
        )}

        {error && <p className="error">{error}</p>}
      </div>
    </div>
  )
}
