import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface Post {
    id: number;
    title: string;
    body: string;
}

export type { Post };

export const postsApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "postsApi",
    tagTypes: ["Posts"],
    endpoints: (build) => ({
        getPosts: build.query<Pagination<Post>, PageRequest>({
            query: (req) => `api/posts` + queryParamsFromRequest(req),
            providesTags: ['Posts'],
        }),

    }),
});

export const { useGetPostsQuery } = postsApiSlice;