import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";
import type {Doctor} from "~/features/authentication/authenticationApiSlice";

interface Post {
    id: number;
    title: string;
    body: string;
    doctor: Doctor;
    replies: Post[];
}

interface PostForm {
    id?: number;
    title?: string;
    parentId?: number;
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
            providesTags: (result) =>
                result
                    ? [
                        ...result.data.map(({id}) => ({type: 'Posts' as const, id})),
                        {type: 'Posts', id: 'LIST'},
                    ]
                    : [{type: 'Posts', id: 'LIST'}],
        }),

        getPost: build.query<Post, {postId: string}>({
            query: ({postId}) => `api/posts/${postId}`,
            providesTags: (result, error, {postId}) => [{type: 'Posts', id: postId}],
        }),

        createPost: build.mutation<Post, PostForm>({
            query: (req) => ({
                url: `api/posts`,
                method: "POST",
                body: req,
            }),
            invalidatesTags: [{type: 'Posts', id: 'LIST'}],
        }),

        editPost: build.mutation<Post, PostForm>({
            query: (req) => ({
                url: `api/posts/${req.id}/edit`,
                method: "PUT",
                body: req,
            }),
            invalidatesTags: (result, error, {id}) => [{type: 'Posts', id: id}],
        }),
    }),
});

export const { useGetPostsQuery, useGetPostQuery, useCreatePostMutation, useEditPostMutation } = postsApiSlice;