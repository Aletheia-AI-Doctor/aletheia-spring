import type { Route } from "./+types/index";
import InfiniteScrollList from "~/components/infinite-scroll";
import {type Post, useGetPostsQuery} from "~/features/community/postsApiSlice";
import Card from "~/components/Card";
import Loading from "~/components/Loading";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community" },
        { name: "description", content: "Welcome to the Doxa community!" },
    ];
}


export default function Doxa() {

    return (
        <div>
            <h1>Doxa</h1>
            <p>Welcome to the Doxa community!</p>
            <Card className="grid grid-cols-12 w-full mt-8 gap-8">
                <InfiniteScrollList
                    hook={useGetPostsQuery}
                    loadingComponent={(
                        <div className="col-span-12">
                            <Loading />
                        </div>
                    )}
                    emptyComponent={<div className="col-span-12 w-full">No posts found.</div>}
                    renderItem={(post: Post) => (
                        <div key={post.id} className="col-span-12 sm:col-span-6 md:col-span-4">
                            {post.title}
                        </div>
                    )}
                />
            </Card>
        </div>
    );
}