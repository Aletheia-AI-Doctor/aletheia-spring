import type { Route } from "./+types/confirm-email";
import { useSearchParams } from 'react-router';

export function meta({}: Route.MetaArgs) {
    return [{ title: "Confirm Email" }];
}

const ConfirmEmail = () => {
    const [searchParams] = useSearchParams();
    const status = searchParams.get('status');

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 bg-white shadow-md rounded-lg text-center">
                {status === 'success' ? (
                    <div>
                        <svg className="w-20 h-20 mx-auto text-green-500 mb-4" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <h2 className="text-2xl font-semibold text-green-600">Email Confirmed!</h2>
                        <p className="text-gray-600 mt-2">Your doctor has been successfully confirmed.</p>
                    </div>
                ) : status === 'error' ? (
                    <div>
                        <svg className="w-20 h-20 mx-auto text-red-500 mb-4" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M4.93 4.93a10 10 0 0114.14 0m-14.14 0a10 10 0 000 14.14m14.14 0a10 10 0 010-14.14M12 2a10 10 0 0110 10 10 10 0 01-10 10 10 10 0 01-10-10 10 10 0 0110-10z" />
                        </svg>
                        <h2 className="text-2xl font-semibold text-red-600">Confirmation Failed</h2>
                        <p className="text-gray-600 mt-2">Invalid or expired token. Please try again.</p>
                    </div>
                ) : (
                    <div>
                        <svg className="w-20 h-20 mx-auto text-gray-400 mb-4 animate-spin" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M4 12a8 8 0 018-8m0 0a8 8 0 018 8m-8-8v4m0 4v4" />
                        </svg>
                        <h2 className="text-2xl font-semibold text-gray-600">Loading...</h2>
                        <p className="text-gray-600 mt-2">Please wait while we confirm your email.</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ConfirmEmail;
