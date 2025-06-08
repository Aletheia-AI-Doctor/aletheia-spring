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
            <p>Welcome to the Doxa community!</p>

            <Button onClick={() => setOpen(true)} className="mt-4 mb-4" width="w-auto">
                Ask Question
            </Button>
            <Modal open={open} onClose={() => setOpen(false)} title="Ask Question">
                <CreatePost onSubmit={handelPostCreate} />
            </Modal>
            <div className="grid grid-cols-12 w-full mt-8 gap-8">
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
                        <Card padding="p-0" className="col-span-12 xl:col-span-6 transition-all hover:scale-105 duration-200 hover:shadow-lg">
                        <Link
                            key={post.id}
                            to={`/doxa/${post.id}`}
                            className="h-full w-full block"
                        >
                            <div className="p-6">
                                <DoctorMedia doctor={post.doctor} />
                                <h2 className="text-gray-900 text-lg mt-4">{post.title}</h2>

                                <div
                                    className="prose"
                                    dangerouslySetInnerHTML={{__html: post.body.slice(0, 200)}}
                                />
                            </div>
                            </Link>
                        </Card>
                    )}
                />
            </div>
        </div>
    );
}