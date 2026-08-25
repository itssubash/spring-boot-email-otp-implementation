import { useState } from 'react'
import { Link } from 'react-router-dom'
import { apiCall } from '../api.js'

const inputCls =
  'w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 placeholder-slate-400 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/30'

export default function ForgotPassword() {
  const [step, setStep] = useState('email')
  const [email, setEmail] = useState('')
  const [otp, setOtp] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [error, setError] = useState('')
  const [info, setInfo] = useState('')
  const [loading, setLoading] = useState(false)

  const submitEmail = async (e) => {
    e.preventDefault()
    setError('')
    setInfo('')
    setLoading(true)
    try {
      const { ok, data } = await apiCall('/forgot-password', { email })
      if (!ok) {
        setError(data.message || 'Failed to send OTP')
        return
      }
      setInfo('OTP sent to your email. It is valid for 5 minutes.')
      setStep('otp')
    } catch {
      setError('Cannot reach server')
    } finally {
      setLoading(false)
    }
  }

  const submitReset = async (e) => {
    e.preventDefault()
    setError('')
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match')
      return
    }
    setLoading(true)
    try {
      const { ok, data } = await apiCall('/reset-password', { email, otp, newPassword })
      if (!ok) {
        setError(data.message || 'Failed to reset password')
        return
      }
      setStep('done')
    } catch {
      setError('Cannot reach server')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-2xl">
      <h1 className="mb-1 text-2xl font-bold text-slate-800">Forgot password</h1>
      <p className="mb-6 text-sm text-slate-500">
        {step === 'email' && 'Enter your email to receive an OTP'}
        {step === 'otp' && `Enter the OTP sent to ${email}`}
        {step === 'done' && 'All done!'}
      </p>

      {error && (
        <div className="mb-4 rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">{error}</div>
      )}
      {info && step === 'otp' && (
        <div className="mb-4 rounded-lg bg-blue-50 px-4 py-3 text-sm text-blue-700">{info}</div>
      )}

      {step === 'email' && (
        <form onSubmit={submitEmail} className="space-y-4">
          <input
            className={inputCls}
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-60"
          >
            {loading ? 'Sending...' : 'Send OTP'}
          </button>
        </form>
      )}

      {step === 'otp' && (
        <form onSubmit={submitReset} className="space-y-4">
          <input
            className={`${inputCls} tracking-[0.4em] text-center text-lg`}
            placeholder="000000"
            maxLength={6}
            value={otp}
            onChange={(e) => setOtp(e.target.value.replace(/\D/g, ''))}
            required
          />
          <input
            className={inputCls}
            type="password"
            placeholder="New password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
          <input
            className={inputCls}
            type="password"
            placeholder="Confirm new password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-lg bg-indigo-600 py-2.5 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-60"
          >
            {loading ? 'Resetting...' : 'Reset password'}
          </button>
          <button
            type="button"
            onClick={() => {
              setStep('email')
              setError('')
              setInfo('')
              setOtp('')
              setNewPassword('')
              setConfirmPassword('')
            }}
            className="w-full text-sm font-medium text-slate-500 hover:text-slate-700"
          >
            Use a different email / resend OTP
          </button>
        </form>
      )}

      {step === 'done' && (
        <div className="space-y-4">
          <div className="rounded-lg bg-green-50 px-4 py-3 text-sm text-green-700">
            Your password has been reset successfully.
          </div>
          <Link
            to="/login"
            className="block w-full rounded-lg bg-indigo-600 py-2.5 text-center text-sm font-semibold text-white transition hover:bg-indigo-700"
          >
            Go to sign in
          </Link>
        </div>
      )}

      {step !== 'done' && (
        <p className="mt-4 text-center text-sm text-slate-500">
          Remembered it?{' '}
          <Link to="/login" className="font-medium text-indigo-600 hover:underline">
            Back to sign in
          </Link>
        </p>
      )}
    </div>
  )
}
