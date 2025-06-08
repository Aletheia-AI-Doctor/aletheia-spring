import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { ROOT_URL } from "~/base/consts";
import { defaultHeaders } from "~/base/helpers";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface notfication{
 id:number;
 votes:number;
 post:string
}

export type { notfication };
export const notficationApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers) => defaultHeaders(headers),
    }),
    reducerPath: "notficationApi",
    tagTypes: ["notfication"],
    endpoints: (build) => ({
        getNotfication: build.query<Pagination<notfication>, void>({
            query: () => `api/notfications/getLastNotification`,
            providesTags: ['notfication'],
        }),
    })
});
export const { useGetNotficationQuery } = notficationApiSlice;