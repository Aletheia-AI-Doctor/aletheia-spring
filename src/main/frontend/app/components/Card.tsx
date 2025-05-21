import type {ReactNode} from "react";

export default function Card({children, className}: { children: ReactNode, className?: string }) {
    return (
        <div className={`bg-white px-4 py-3 rounded-lg shadow-lg ${className}`}>
            {children}
        </div>
    );
}