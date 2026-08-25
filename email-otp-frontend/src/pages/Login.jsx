import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { apiCall } from '../api.js'

const inputCls =
  'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 placeholder-slate-400 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/30'

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const onSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const { ok, data } = await apiCall('/login', form)
      if (!ok) {
        setError(data.message || 'Invalid credentials')
        return
      }
      localStorage.setItem('user', JSON.stringify(data.user))
      if (data.tokenPair) localStorage.setItem('tokenPair', JSON.stringify(data.tokenPair))
      navigate('/dashboard')
    } catch {
      setError('Cannot reach server')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-2xl">
      <h1 className="mb-1 text-2xl font-bold text-slate-800">Welcome back</h1>
      <p className="mb-6 text-sm text-slate-500">Sign in to your account</p>

      {error && (
        <div className="mb-4 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">{error}</div>
      )}

      <form onSubmit={onSubmit} className="space-y-4">
        <input
          className={inputCls}
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={onChange}
          required
        />
        <input
          className={inputCls}
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={onChange}
          required
        />
        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-60"
        >
          {loading ? 'Signing in...' : 'Sign in'}
        </button>
      </form>

      <div className="mt-4 flex justify-between text-sm">
        <Link to="/forgot-password" className="font-medium text-indigo-600 hover:underline">
          Forgot password?
        </Link>
        <Link to="/register" className="font-medium text-indigo-600 hover:underline">
          Create account
        </Link>
      </div>
    </div>
  )
}
