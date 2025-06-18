// notficationApiSlice.ts
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import { NotificationDto } from "~/types/notification";

import { PostDto } from "~/types/post";

export interface NotificationDto {
    id: number;
    userId: number;
    replies: PostDto[];
    post: PostDto;
    vote: number;
    createdAt: string;
}

export const notificationApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "notificationApi",
    tagTypes: ["Notification"],
    endpoints: (build) => ({
        getNotifications: build.query<NotificationDto, void>({
            query: () => `/api/notifications/getLastNotification`,
            providesTags: ['Notification'],
        }),
    })
});

export const { useGetNotificationsQuery } = notificationApiSlice;