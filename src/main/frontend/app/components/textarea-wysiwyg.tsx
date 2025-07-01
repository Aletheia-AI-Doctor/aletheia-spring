import 'trix/dist/trix.css';
import Trix from "trix";
import React, {useEffect, useRef} from "react";
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";

interface TextareaWysiwygProps {
    value: string;
    onChange: (html: string) => void;
    disabled?: boolean;
}

export default function TextareaWysiwyg({ value, onChange, disabled }: TextareaWysiwygProps) {
    const trixEditorRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        const editor = trixEditorRef.current!;

        const handleChange = (event: Event) => {
            const target = event.target as HTMLInputElement;
            onChange(target.innerHTML);
        };

        const handleAttachmentAdd = async (event: any) => {
            const attachment = event.attachment;
            if (!attachment.file) return;

            try {
                // Only process images
                if (!attachment.file.type.startsWith("image/")) {
                    throw new Error("Only images are supported");
                }

                // Upload image to server
                const imageUrl = await uploadImage(attachment.file);

                // Update attachment with server URL
                attachment.setAttributes({
                    url: imageUrl,
                    href: imageUrl
                });
            } catch (error) {
                console.error("Upload failed", error);
                attachment.remove();
            }
        };

        editor.addEventListener("trix-change", handleChange);
        editor.addEventListener("trix-attachment-add", handleAttachmentAdd);

        return () => {
            editor.removeEventListener("trix-change", handleChange);
            editor.removeEventListener("trix-attachment-add", handleAttachmentAdd);
        };
    }, []);

    const uploadImage = async (file: File): Promise<string> => {
        const formData = new FormData();
        formData.append('image', file);

        const headers = new Headers();
        defaultHeaders(headers);
        headers.delete('Content-Type');

        const response = await fetch(`${ROOT_URL}/api/posts/upload`, {
            method: "PUT",
            headers,
            body: formData
        });

        if (!response.ok) {
            throw new Error("Image upload failed");
        }

        const data = await response.json();
        return data.imageUrl;
    };

    return (
        <div className="prose w-full min-w-full">
            <input
                type="hidden"
                id="trix"
                value={value}
            />
            { /* @ts-ignore */}
            <trix-editor
                disabled={disabled}
                ref={trixEditorRef}
                className="min-h-[240px]"
                input="trix"
            />
        </div>
    );
}