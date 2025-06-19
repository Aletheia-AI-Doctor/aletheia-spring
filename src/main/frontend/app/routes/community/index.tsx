import type { Route } from "./+types/index";
import InfiniteScrollList from "~/components/infinite-scroll";
import {type Post, useGetPostsQuery} from "~/features/community/postsApiSlice";
import Card from "~/components/Card";
import Loading from "~/components/Loading";
import CreatePost from "~/components/create-post";
import {Link, useNavigate} from "react-router";
import Title from "~/components/title";
import Button from "~/components/button";
import Modal from "~/components/modal";
import {useState} from "react";
import DoctorMedia from "~/components/doctor-media";
import Votes from "~/components/votes";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Doxa Community" },
        { name: "description", content: "Welcome to the Doxa community!" },
    ];
}


export default function Doxa() {
    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [refresh, setRefresh] = useState(0);

    function handelPostCreate(post: Post) {
        setOpen(false);

        setRefresh(ref => ref+1);

        navigate(`/doxa/${post.id}`);
    }

    return (
        <div>
            <Title>Doxa</Title>

            <Button className="mt-6" onClick={() => setOpen(true)} width="w-auto">
                Ask Question
            </Button>
            <Modal size="lg" open={open} onClose={() => setOpen(false)} title="Ask Question">
                <CreatePost onSubmit={handelPostCreate} />
            </Modal>
            <div className="grid grid-cols-12 w-full mt-4 gap-8">
                <InfiniteScrollList
                    hook={useGetPostsQuery}
                    refresh={refresh}
                    loadingComponent={(
                        <div className="col-span-12">
                            <Loading />
                        </div>
                    )}
                    emptyComponent={<div className="col-span-12 w-full">No posts found.</div>}
                    renderItem={(post: Post) => (
                        <Card key={post.id} padding="p-0" className="col-span-12 flex flex-col justify-between">
                            <Link
                                to={`/doxa/${post.id}`}
                                className="w-full h-full block"
                            >
                                <div className="p-6">
                                    <div className="flex items-start justify-between gap-2">
                                        <DoctorMedia doctor={post.doctor}/>
                                        <div className="text-xs font-medium text-gray-400">
                                            {new Date(post.createdAt).toLocaleDateString()} {new Date(post.createdAt).toLocaleTimeString()}
                                        </div>
                                    </div>
                                    <h2 className="text-gray-900 text-lg mt-4">{post.title}</h2>

                                    <div
                                        className="prose"
                                        dangerouslySetInnerHTML={{__html: post.body.slice(0, 200)}}
                                    />
                                </div>
                            </Link>

                            <div className="flex items-center">
                                <div className="m-4">
                                    <Votes post={post} />
                                </div>
                                <div>
                                    <div className="text-gray-500 text-sm mt-2">
                                        {post.replies && post.replies.length > 0
                                            ? `${post.replies.length} replies`
                                            : "No replies yet"}
                                    </div>
                                </div>
                            </div>
                        </Card>
                    )}
                />
            </div>
        </div>
    );
}