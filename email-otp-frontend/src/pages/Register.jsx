import { useState } from 'react'
import { Link } from 'react-router-dom'
import { apiCall } from '../api.js'

const inputCls =
  'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 placeholder-slate-400 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/30'

export default function Register() {
  const [form, setForm] = useState({ username: '', email: '', password: '', confirmPassword: '' })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const onSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match')
      return
    }
    setLoading(true)
    try {
      const { ok, status, data } = await apiCall('/register', {
        username: form.username,
        email: form.email,
        password: form.password,
      })
      if (!ok) {
        setError(status === 409 ? 'Username or email already exists' : data.message || 'Registration failed')
        return
      }
      setSuccess(`Account created for ${data.username}. You can sign in now.`)
      setForm({ username: '', email: '', password: '', confirmPassword: '' })
    } catch {
      setError('Cannot reach server')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-2xl">
      <h1 className="mb-1 text-2xl font-bold text-slate-800">Create account</h1>
      <p className="mb-6 text-sm text-slate-500">Register to get started</p>

      {error && (
        <div className="mb-4 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">{error}</div>
      )}
      {success && (
        <div className="mb-4 rounded-lg bg-green-50 px-4 py-3 text-sm text-green-700">{success}</div>
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
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
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
        <input
          className={inputCls}
          type="password"
          name="confirmPassword"
          placeholder="Confirm password"
          value={form.confirmPassword}
          onChange={onChange}
          required
        />
        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-60"
        >
          {loading ? 'Creating...' : 'Register'}
        </button>
      </form>

      <p className="mt-4 text-center text-sm text-slate-500">
        Already have an account?{' '}
        <Link to="/login" className="font-medium text-indigo-600 hover:underline">
          Sign in
        </Link>
      </p>
    </div>
  )
}
