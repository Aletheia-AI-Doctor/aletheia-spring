import React from "react";

interface ReadMoreProps {
    children?: React.ReactNode;
    maxLength?: number;
    showReadMore?: boolean;
}

export default function ReadMore({ children, maxLength = 100, showReadMore = true }: ReadMoreProps) {
    const [isExpanded, setIsExpanded] = React.useState(false);

    return (
        <div>
            {isExpanded || !children || typeof children !== "string" || children.length <= maxLength ? (
                <span>{children}</span>
            ) : (
                    <span>
                    {children.slice(0, maxLength)}...
                        {showReadMore && (
                        <button
                            onClick={() => setIsExpanded(true)}
                            className="text-blue-500 hover:underline ml-1"
                        >
                            Read more
                        </button>
                        )}
                </span>
            )}
            {isExpanded && showReadMore && (
                <button
                    onClick={() => setIsExpanded(false)}
                    className="text-blue-500 hover:underline ml-1"
                >
                    Show less
                </button>
            )}
        </div>
    );
}