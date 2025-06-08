import React, {useCallback, useState} from 'react';
import { useAppSelector } from "~/base/hooks";
import Card from "~/components/Card";
import Button from "~/components/button";
import TextareaWysiwyg from "~/components/textarea-wysiwyg";
import {useCreatePostMutation} from "~/features/community/postsApiSlice";
import Input from "~/components/input";

interface CreatePostProps {
    parentId?: number;
    onSubmit?: () => void;
}

export default function TrixEditor({parentId, onSubmit}: CreatePostProps) {
    const doctor = useAppSelector(state => state.auth.doctor);
    const [value, onChange] = useState<string>("");
    const [title, setTitle] = useState<string>("");
    const [show, setShow] = useState<boolean>(!parentId);

    const [createPost, {isLoading, isSuccess}] = useCreatePostMutation();

    const isReply = parentId != undefined;

    const handleSubmit = useCallback(async (e: React.FormEvent) => {
        e.preventDefault();

        const response = await createPost({
            body: value,
            title,
            parentId,
        });

        if(response.error) {
            // @ts-ignore
            console.error("Failed to create post:", response.error.message);
            return;
        }

        onChange("");
        setTitle("");

        if(isReply) {
            setShow(false);
        }

        onSubmit && onSubmit();
    }, [value]);

    return (
        show ? (
                <div className="mt-6">
                    <div className="flex justify-center space-x-4">
                        <div className="shrink-0">
                            <img
                                alt="Doctor profile"
                                src={doctor!.image}
                                className="inline-block size-10 rounded-full"
                            />
                        </div>
                        <div className="flex">
                            <form onSubmit={handleSubmit}>
                                {!parentId && (
                                    <Input
                                        id="title"
                                        label="Title"
                                        value={title}
                                        onChange={(e) => setTitle(e.target.value)}
                                        placeholder="Title"
                                        className="mb-4"
                                    />
                                )}

                                <TextareaWysiwyg value={value} onChange={onChange}/>

                                <div className="flex justify-end">
                                    {isReply && (
                                        <Button width="w-auto" type="button" color="gray" className="mt-4 mr-2" onClick={() => setShow(false)}>
                                            Cancel
                                        </Button>
                                    )}
                                    <Button
                                        className="mt-4"
                                        type="submit"
                                        width="w-auto"
                                        disabled={!value.trim() || isLoading}
                                    >
                                        {isLoading ? "Posting..." : "Post"}
                                    </Button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            ) : (
            <div>
                <button type="button" className="cursor-pointer bg-gray-100 hover:bg-gray-200  rounded px-2 py-0.5 text-sm" onClick={() => setShow(true)}>
                    Reply
                </button>
            </div>
        )

    );
}