import type { Route } from "./+types/show";
import {useParams} from "react-router";
import {type Post, useGetPostQuery} from "~/features/community/postsApiSlice";
import Loading from "~/components/Loading";
import CreatePost from "~/components/create-post";
import Title from "~/components/title";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community - Post" },
        { name: "description", content: "View post" },
    ];
}

function PostComponent({ post, refetch, parent }: { post: Post, refetch: () => void, parent?: boolean }) {
    return (
        <div className={!parent ? 'pl-4 ml-2 mt-4' : ''}>
            <div className="bg-white shadow-sm rounded-lg p-4">
                {/* Doctor info */}
                <div className="flex items-center gap-3 mb-2">
                    <img
                        src={post.doctor.image}
                        alt={post.doctor.name}
                        className="w-8 h-8 rounded-full object-cover"
                    />
                    <span className="text-sm font-medium text-gray-800">{post.doctor.name}</span>
                </div>

                {/* Post content */}
                {parent && <Title>{post.title}</Title>}
                <div
                    className={"mt-2 text-gray-700 " + (parent ? "prose" : "prose-sm")}
                    dangerouslySetInnerHTML={{ __html: post.body }}
                />

                {/* Reply form */}
                <div className="mt-4">
                    <CreatePost onSubmit={refetch} parentId={post.id} />
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