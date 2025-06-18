// notficationApiSlice.ts
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import type { Post } from "./postsApiSlice";
import type { PageRequest, Pagination } from "~/types/pagination";


interface Notification {
    length: number;
    replies: Post["replies"];
    vote: number;
    createdAt: string;
}
export type { Notification };
export const notificationApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "notificationApi",
    tagTypes: ["Notification"],
    endpoints: (build) => ({
        getNotifications: build.query<Notification, void>({
            query: () => `/api/notifications/getLastNotification`,
            providesTags: ['Notification'],
        }),
    })
});

export const { useGetNotificationsQuery } = notificationApiSlice;