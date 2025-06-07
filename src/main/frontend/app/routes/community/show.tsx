import type { Route } from "./+types/show";
import {useParams} from "react-router";
import {useGetPostQuery} from "~/features/community/postsApiSlice";
import Loading from "~/components/Loading";
import Title from "~/components/title";
import Card from "~/components/Card";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community - Post" },
        { name: "description", content: "View post" },
    ];
}


export default function DoxaPost() {
    const {id} = useParams<{ id: string }>();

    const {data: post, isLoading} = useGetPostQuery({postId: id!});

    if (isLoading || !post) {
        return <Loading />;
    }

    return (
        <div>
            <Card>
                <Title>{post.title}</Title>

                <div className="prose mt-4">
                    {post.body}
                </div>
            </Card>
        </div>
    );
}