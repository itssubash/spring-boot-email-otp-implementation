import { Navigate, Link } from 'react-router-dom'

export default function Dashboard() {
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  if (!user) return <Navigate to="/login" replace />

  const logout = () => {
    localStorage.clear()
    window.location.href = '/login'
  }

  return (
    <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-2xl">
      <h1 className="mb-1 text-2xl font-bold text-slate-800">Signed in</h1>
      <p className="mb-6 text-sm text-slate-500">You are logged in</p>

      <dl className="mb-6 space-y-3 rounded-xl bg-slate-50 p-4 text-sm">
        <div className="flex justify-between">
          <dt className="text-slate-500">User ID</dt>
          <dd className="font-medium text-slate-800">{user.userId}</dd>
        </div>
        <div className="flex justify-between">
          <dt className="text-slate-500">Username</dt>
          <dd className="font-medium text-slate-800">{user.username}</dd>
        </div>
        <div className="flex justify-between">
          <dt className="text-slate-500">Email</dt>
          <dd className="font-medium text-slate-800">{user.email}</dd>
        </div>
      </dl>

      <button
        onClick={logout}
        className="w-full rounded-lg bg-red-600 py-2.5 text-sm font-semibold text-white transition hover:bg-red-700"
      >
        Log out
      </button>

      <p className="mt-4 text-center text-sm">
        <Link to="/forgot-password" className="font-medium text-indigo-600 hover:underline">
          Try forgot-password flow
        </Link>
      </p>
    </div>
  )
}
