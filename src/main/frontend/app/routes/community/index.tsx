import type { Route } from "./+types/index";
import InfiniteScrollList from "~/components/infinite-scroll";
import {type Post, useGetPostsQuery} from "~/features/community/postsApiSlice";
import Card from "~/components/Card";
import Loading from "~/components/Loading";
import ReadMore from "~/components/readmore";
import {Link} from "react-router";
import Title from "~/components/title";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community" },
        { name: "description", content: "Welcome to the Doxa community!" },
    ];
}


export default function Doxa() {

    return (
        <div>
            <Title>Doxa</Title>
            <p>Welcome to the Doxa community!</p>
            <div className="grid grid-cols-12 w-full mt-8 gap-8">
                <InfiniteScrollList
                    hook={useGetPostsQuery}
                    loadingComponent={(
                        <div className="col-span-12">
                            <Loading />
                        </div>
                    )}
                    emptyComponent={<div className="col-span-12 w-full">No posts found.</div>}
                    renderItem={(post: Post) => (
                        <Link
                            key={post.id}
                            to={`/doxa/${post.id}`}
                            className="col-span-12 xl:col-span-6 transition-all hover:scale-105 duration-200 hover:shadow-lg"
                        >
                        <Card className="col-span-12 xl:col-span-6">
                            <h2 className="text-gray-900 text-lg">{post.title}</h2>

                            <div className="prose">
                                <ReadMore showReadMore={false} maxLength={200}>{post.body}</ReadMore>
                            </div>
                        </Card>
                        </Link>
                    )}
                />
            </div>
        </div>
    );
}