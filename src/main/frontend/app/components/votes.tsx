import {ChevronUpIcon, ChevronDownIcon} from "@heroicons/react/24/outline";
import {type Post, useSetVoteMutation} from "~/features/community/postsApiSlice";
import {useEffect, useState} from "react";

interface VotesProps {
    post: Post;
}

export default function Votes({post}: VotesProps) {
    const [votes, setVotes] = useState(post.votes);
    const [myVote, setMyVote] = useState(post.myVote);
    const [setPostVote, {isLoading, isSuccess, data}] = useSetVoteMutation();

    function handleVote(positive: boolean) {
        setPostVote({postId: post.id, vote: positive});

        if (myVote === (positive ? 1 : -1)) {
            setVotes(votes - (positive ? 1 : -1));
            return;
        }

        if(myVote === (positive ? -1 : 1)) {
            setVotes(votes + (positive ? 2 : -2));
        } else {
            if (myVote !== 0) {
                setVotes(votes + (positive ? 1 : -1) * 2);
            }
            setMyVote(positive ? 1 : -1);
        }
    }

    useEffect(() => {
        if(!isLoading && isSuccess && data) {
            setVotes(data.votes);
            setMyVote(data.myVote);
        }
    }, [isLoading, isSuccess]);

    return (
        <div className="flex items-center border border-gray-600 rounded-full">
            <button
                onClick={() => handleVote(true)}
                className={"hover:bg-blue-100 rounded-l-full transition-colors duration-200 pr-2 " + (myVote === 1 ? "text-blue-500 bg-blue-200" : "")}
                aria-label="Upvote"
            >
                <ChevronUpIcon className="size-7 pl-2 text-gray-900" />
            </button>
            <span className="px-2 text-sm text-gray-700">{votes}</span>
            <button
                onClick={() => handleVote(false)}
                className={"hover:bg-red-100 rounded-r-full transition-colors duration-200 pl-2 " + (myVote === -1 ? "text-red-500 bg-red-200" : "")}
                aria-label="Downvote"
            >
                <ChevronDownIcon className="size-7 pr-2" />
            </button>
        </div>
    );
}