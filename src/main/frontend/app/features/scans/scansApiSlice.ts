import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react"
import {ROOT_URL} from "~/base/consts";
import {defaultHeadersFileUpload} from "~/base/helpers";

interface Diagnosis {
    id: number;
    name: string;
}
interface Model {
    id: string;
    name: string;
    path: string;
    slug: string;
}

export type { Diagnosis, Model };

// Define a service using a base URL and expected endpoints
export const scansApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: ROOT_URL,
        prepareHeaders: (headers, {}) => defaultHeadersFileUpload(headers),
    }),
    reducerPath: "scansApi",
    tagTypes: ["Scans", "Models"],
    endpoints: build => ({

        getModels: build.query<Model[], void>({
            query: () => "api/models",
            providesTags: ['Models'],
        }),

        uploadScan: build.mutation<Diagnosis, { scan: File, model:string }>({
            query: ({scan, model}) => {

                const formData = new FormData();
                formData.append("scan", scan);

                return {
                    url: `api/models/${model}/predict`,
                    method: "PUT",
                    headers: {
                        "Content-Type": "multipart/form-data;"
                    },
                    body: formData,
                    formData: true,
                };
            },
        }),
    }),
})



export const {useUploadScanMutation, useGetModelsQuery} = scansApiSlice