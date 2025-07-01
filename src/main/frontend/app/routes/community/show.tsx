import type { Route } from "./+types/show";
import {useParams} from "react-router";
import {type Post, useEditPostMutation, useGetPostQuery} from "~/features/community/postsApiSlice";
import Loading from "~/components/Loading";
import CreatePost from "~/components/create-post";
import Title from "~/components/title";
import DoctorMedia from "~/components/doctor-media";
import {useAppDispatch, useAppSelector} from "~/base/hooks";
import {type FormEvent, useState} from "react";
import Button from "~/components/button";
import TextareaWysiwyg from "~/components/textarea-wysiwyg";
import Votes from "~/components/votes";
import {sendErrorNotification, sendSuccessNotification} from "~/features/notifications/notificationSlice";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community - Post" },
        { name: "description", content: "View post" },
    ];
}

function PostComponent({ post, refetch, parent }: { post: Post, refetch: () => void, parent?: boolean }) {
    const currentDoctor = useAppSelector((state) => state.auth.doctor);
    const [editing, setEditing] = useState(false);
    const [updatePost, {isLoading}] = useEditPostMutation();
    const [content, setContent] = useState(post.body);

    const dispatch = useAppDispatch();
    async function handleSubmit(event : FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const response = await updatePost({
            id: post.id,
            body: content,
        });

        if (response.error) {
            // @ts-ignore
            console.error(response.error);
            dispatch(sendErrorNotification("Post must have a body"));
            return;
        }

        dispatch(sendSuccessNotification("Post saved successfully!"));

        setEditing(false);
        refetch();
    }

    return (
        <div className={!parent ? 'pl-4 ml-2 mt-4' : ''} id={"post-" + post.id}>
            <div className="bg-white shadow-sm rounded-lg p-4 relative">
                <div className="flex items-start justify-between gap-2">
                    <DoctorMedia doctor={post.doctor}/>
                    <div className="text-xs font-medium text-gray-400">
                        {new Date(post.createdAt).toLocaleDateString()} {new Date(post.createdAt).toLocaleTimeString()}
                    </div>
                </div>

                <div className={"flex items-center " + (parent ? "justify-between" : "justify-end")}>
                    {parent && <Title>{post.title}</Title>}
                    {currentDoctor!.id === post.doctor.id && !editing && (
                        <Button className="absolute right-0 top-0 mr-4 mt-12" width="w-auto" color="gray"
                                onClick={() => setEditing(true)}>Edit</Button>
                    )}
                </div>

                {editing ? (
                    <div className="mx-auto w-full mt-6">
                        <form onSubmit={handleSubmit}>
                            <TextareaWysiwyg value={content} onChange={setContent}/>

                            <div className="mt-4 flex justify-end">
                                <Button
                                    width="w-auto"
                                    type="submit"
                                    disabled={isLoading}
                                    className="mr-2"
                                >
                                    Save
                                </Button>
                                <Button
                                    width="w-auto"
                                    onClick={() => setEditing(false)}
                                    color="gray"
                                >
                                    Cancel
                                </Button>
                            </div>
                        </form>
                    </div>
                ) : (
                    <div
                        className={"text-gray-700 " + (parent ? "mt-2 prose" : "prose-sm")}
                        dangerouslySetInnerHTML={{__html: post.body}}
                    />
                )}

                <div className="mt-4 flex items-center justify-between">
                    <Votes post={post}/>
                </div>

                <div className="mt-4">
                    <CreatePost onSubmit={refetch} parentId={post.id}/>
                </div>
            </div>

            {/* Replies */}
            {post.replies && post.replies.length > 0 && (
                <div className="mt-4 space-y-2 border-l-2 border-gray-300">
                    {post.replies.map((reply) => (
                        <PostComponent
                            refetch={refetch}
                            key={reply.id}
                            post={reply}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}

export default function DoxaPost() {
    const {id} = useParams<{ id: string }>();

    const {data: post, isLoading, refetch} = useGetPostQuery({postId: id!});

    if (!post) {
        return <Loading />;
    }

    return (
        <div>
            <PostComponent parent={true} refetch={refetch} post={post} />
        </div>
    );
}