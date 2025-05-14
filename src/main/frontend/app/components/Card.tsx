import type {ReactNode} from "react";

export default function Card({children}: { children: ReactNode }) {
    return (
        <div className="bg-white px-4 py-3 rounded-lg shadow-lg">
            {children}
        </div>
    );
}