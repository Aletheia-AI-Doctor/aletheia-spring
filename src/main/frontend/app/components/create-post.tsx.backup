import React, { useRef } from 'react';
import MdEditor, { Plugins } from 'react-markdown-editor-lite';
import MarkdownIt from 'markdown-it';
import 'react-markdown-editor-lite/lib/index.css';

interface CreatePostProps {
    value: string;
    onChange: (markdown: string) => void;
    onImageUpload: (file: File) => Promise<string>;
}

MdEditor.use(Plugins.TabInsert);
MdEditor.use(Plugins.FullScreen);

const CreatePost: React.FC<CreatePostProps> = ({ value, onChange, onImageUpload }) => {
    const editorRef = useRef<MdEditor>(null);
    const mdParser = new MarkdownIt();

    const handleImageUpload = async (file: File) => {
        try {
            const imageUrl = await onImageUpload(file);
            if (editorRef.current) {
                const editor = editorRef.current;
                const cursorPositionStart = editor.getSelection().start;
                const cursorPositionEnd = editor.getSelection().end;
                const markdownImage = `![${file.name}](${imageUrl})`;
                editor.insertText(markdownImage, true, {start:cursorPositionStart, end:cursorPositionEnd});
            }
        } catch (error) {
            console.error('Image upload failed:', error);
        }
    };

    // Custom toolbar components
    const CustomToolbar = () => (
        <div className="custom-toolbar">
            <button
                className="toolbar-button"
                title="Bold"
                onClick={() => editorRef.current?.insertText('**Bold text**')}
            >
                <strong>B</strong>
            </button>
            <button
                className="toolbar-button"
                title="Italic"
                onClick={() => editorRef.current?.insertText('*Italic text*')}
            >
                <em>I</em>
            </button>
            <label className="toolbar-button" title="Upload Image">
                <input
                    type="file"
                    accept="image/*"
                    style={{ display: 'none' }}
                    onChange={(e) => {
                        if (e.target.files?.[0]) handleImageUpload(e.target.files[0]);
                        e.target.value = '';
                    }}
                />
                <span>📷</span>
            </label>
            <button
                className="toolbar-button"
                title="Custom Button"
                onClick={() => alert('Custom button clicked!')}
            >
                ✨
            </button>
        </div>
    );

    return (
        <div className="post-editor">
            <CustomToolbar />
            <MdEditor
                ref={editorRef}
                value={value}
                style={{ height: '500px' }}
                renderHTML={(text) => mdParser.render(text)}
                onChange={({ text }) => onChange(text)}
                view={{ menu: true, md: true, html: true }}
                placeholder="Start writing your content here..."
                config={{
                    view: {
                        html: false
                    },
                    table: {
                        maxRow: 5,
                        maxCol: 6
                    }
                }}
            />
        </div>
    );
};

export default CreatePost;