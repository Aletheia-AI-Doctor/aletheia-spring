import type {ReactNode} from "react";

export default function Card({children, className}: { children: ReactNode, className?: string }) {
    return (
        <div className={`bg-white p-6 rounded-lg shadow-lg ${className}`}>
            {children}
        </div>
    );
}