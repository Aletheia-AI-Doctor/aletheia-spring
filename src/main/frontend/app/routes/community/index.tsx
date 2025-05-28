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
            <div className="grid grid-cols-12 w-full">
                <InfiniteScrollList
                    hook={useGetPostsQuery}
                    loadingComponent={(
                        <div className="col-span-12">
                            <Loading />
                        </div>
                    )}
                    renderItem={(post: Post) => (
                        <Card key={post.id} className="col-span-12 sm:col-span-6 md:col-span-4">
                            {post.title}
                        </Card>
                    )}
                />
            </div>
        </div>
    );
}