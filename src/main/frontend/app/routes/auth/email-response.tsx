import type { Route } from "./+types/email-response";
import { useSearchParams } from 'react-router';

export function meta({}: Route.MetaArgs) {
    return [{ title: "Email Response" }];
}

function CheckIcon({color}: { color?: string }) {
    color ??= "text-green-500";
    return (
        <svg className={`w-20 h-20 mx-auto ${color} mb-4`} fill="none" stroke="currentColor" strokeWidth="2"
             viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
        </svg>
    );
}

function ErrorIcon({color}: { color?: string }) {
    color ??= "text-red-500";
    return (
        <svg className={`w-20 h-20 mx-auto ${color} mb-4`} fill="none" stroke="currentColor" strokeWidth="2"
             viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round"
                  d="M12 8v4m0 4h.01M4.93 4.93a10 10 0 0114.14 0m-14.14 0a10 10 0 000 14.14m14.14 0a10 10 0 010-14.14M12 2a10 10 0 0110 10 10 10 0 01-10 10 10 10 0 01-10-10 10 10 0 0110-10z"/>
        </svg>
    );
}

export default function EmailResponse() {
    const [searchParams] = useSearchParams();
    type ResponseType = "error" | "confirmed" | "rejected" | "appeal" | "alreadyConfirmed" | "unknown";
    let statusStr: string = searchParams.get('status') ?? "unknown";
    let status = statusStr.toLowerCase() as ResponseType;
    if (!(status satisfies ResponseType)) {
        status = "unknown";
    }

    const {title, message, icon, color} = {
        confirmed: {
            title: "Email Confirmed!",
            message: "This email has been successfully confirmed.",
            icon: CheckIcon,
            color: "text-green-500",
        },
        alreadyConfirmed: {
            title: "Email Already Confirmed!",
            message: "This email has already been confirmed.",
            icon: ErrorIcon,
            color: "text-red-500",
        },
        rejected: {
            title: "Email Rejected!",
            message: "This email has been rejected.",
            icon: CheckIcon,
            color: "text-orange-500",
        },
        appeal: {
            title: "Appeal Request Submitted!",
            message: "Your appeal request has been successfully submitted.",
            icon: CheckIcon,
            color: "text-green-500",
        },
        error: {
            title: "Error Occurred",
            message: "Invalid or expired token. Please try again.",
            icon: ErrorIcon,
            color: "text-red-500",
        },
        unknown: {
            title: "Unknown Status",
            message: "An unknown error occurred. Please try again later.",
            icon: ErrorIcon,
            color: "text-gray-500",
        },
    }[status];

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 bg-white shadow-md rounded-lg text-center">
                <div>
                    {icon({color})}
                    <h2 className={`text-2xl font-semibold ${color}`}>{title}</h2>
                    <p className="text-gray-600 mt-2">{message}</p>
                </div>
            </div>
        </div>
    );
};
