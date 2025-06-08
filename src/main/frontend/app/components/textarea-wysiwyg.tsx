import 'trix/dist/trix.css';
import Trix from "trix";
import React, {useEffect, useRef} from "react";


interface TextareaWysiwygProps {
    value: string;
    onChange: (html: string) => void;
    disabled?: boolean;
}

export default function TextareaWysiwyg({ value, onChange, disabled }: TextareaWysiwygProps) {
    const trixEditorRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        trixEditorRef.current!.addEventListener("trix-change", (event) => {
            // @ts-ignore
            onChange(event.target!.innerHTML);
        });
    }, []);

    return (
        <div className="prose">
            <input
                type="hidden"
                id="trix"
                value={value}
            />
            { /* @ts-ignore */}
            <trix-editor
                disabled={disabled}
                ref={trixEditorRef} input="trix"/>
        </div>
    );
}