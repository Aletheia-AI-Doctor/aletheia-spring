import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeaders} from "~/base/helpers";
import {type PageRequest, type Pagination, queryParamsFromRequest} from "~/types/pagination";

interface ActivityLog {
    id: number;
    action: string;
    description: string;
    createdAt: string;
}

export type {ActivityLog};

// Define a service using a base URL and expected endpoints
export const doctorActivityApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeaders(headers),
    }),
    reducerPath: "doctorActivityApi",
    tagTypes: ["ActivityLog"],
    endpoints: build => ({

        getAllActivityLogs: build.query<Pagination<ActivityLog>, PageRequest>({
            query: (req) => `api/activities${queryParamsFromRequest(req)}`,
            providesTags: ["ActivityLog"],
        }),

        getRecentActivityLogs: build.query<ActivityLog[], void>({
            query: () => "api/activities/recent"
        }),
    }),
})



export const {useGetRecentActivityLogsQuery, useGetAllActivityLogsQuery, } = doctorActivityApiSlice