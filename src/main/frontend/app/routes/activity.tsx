import Loading from "~/components/Loading";
import {type ActivityLog, useGetAllActivityLogsQuery} from "~/features/doctor/doctorActivityApiSlice";
import InfiniteScrollList from "~/components/infinite-scroll";
import type { Route } from "./+types/activity";

function classNames(...classes: string[]) {
    return classes.filter(Boolean).join(" ");
}

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Activity Logs" },
        { name: "description", content: "View your activity logs" },
    ];
}


export default function Activity() {
    return (
        <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
            <h1 className="text-3xl font-bold text-blue-600 mb-4">Activity Logs</h1>
            <p className="text-gray-700 mb-4">
                Here you can view your recent activity logs.
            </p>
            <ul>
                <InfiniteScrollList
                    hook={useGetAllActivityLogsQuery}
                    renderItem={(event: ActivityLog) => (
                        <li className="relative pb-8">
                            <div className="relative flex space-x-3">
                                <div>
                                <span
                                    className={classNames(
                                        'flex size-8 items-center justify-center rounded-full ring-8 ring-white',
                                        // event.iconBackground,
                                    )}
                                >
                                  {/* <event.icon className="size-5 text-white" /> */}
                                </span>
                                </div>
                                <div className="flex min-w-0 flex-1 justify-between space-x-4 pt-1.5">
                                    <div>
                                        <p className="text-sm text-gray-500">
                                            {event.action}
                                            <span className="font-medium text-gray-900 block">
                      {event.description}
                    </span>
                                        </p>
                                    </div>
                                    <div className="text-right text-sm whitespace-nowrap text-gray-500">
                                        <time dateTime={event.createdAt}>
                                            {new Date(event.createdAt).toLocaleTimeString()}
                                        </time>
                                    </div>
                                </div>
                            </div>
                        </li>
                    )}
                    emptyComponent={
                        <div className="border border-gray-200 bg-gray-50 rounded-lg p-4">
                            <p className="text-gray-700">No activity logs available yet.</p>
                        </div>
                    }
                    loadingComponent={<Loading/>}
                />
            </ul>
        </div>
    );
}