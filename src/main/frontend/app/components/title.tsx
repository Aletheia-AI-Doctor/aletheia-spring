import React from "react";

export default function Title({children, className}: {children: React.ReactNode, className?: string}) {
    return (
        <h1 className={`text-4xl font-bold text-blue-600 ${className}`}>
            {children}
        </h1>
    );
}