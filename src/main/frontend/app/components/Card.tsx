import type {ReactNode} from "react";

export default function Card({children, className, padding}: { children: ReactNode, className?: string, padding?: string }) {
    padding ??= "p-6";

    return (
        <div className={`bg-white ${padding} rounded-lg shadow-lg ${className}`}>
            {children}
        </div>
    );
}