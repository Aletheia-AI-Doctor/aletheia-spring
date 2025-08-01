export default function Loading({message, size, color} : {message?: string, size?: string, color?: string}) {
    size ??= "size-12";
    color ??= "text-black";

    return (
        <div className="flex items-center justify-center flex-col space-y-4">
            <svg className={size + " " + color} fill="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path d="M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,19a8,8,0,1,1,8-8A8,8,0,0,1,12,20Z" opacity=".25"/>
                <circle cx="12" cy="2.5" r="1.5">
                    <animateTransform attributeName="transform" type="rotate" dur="0.75s" values="0 12 12;360 12 12"
                                      repeatCount="indefinite"/>
                </circle>
            </svg>
            {message && <div>{message}...</div>}
        </div>
    );
}