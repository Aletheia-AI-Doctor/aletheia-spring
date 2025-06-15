import { Fragment, useEffect } from 'react';
import { Transition } from '@headlessui/react';
import {
    CheckCircleIcon,
    XCircleIcon,
    ExclamationTriangleIcon,
    InformationCircleIcon,
    XMarkIcon
} from '@heroicons/react/24/outline';
import { useAppDispatch, useAppSelector } from '~/base/hooks';
import { removeNotification, type Notification } from '~/features/notifications/notificationSlice';

const notificationIcons = {
    success: CheckCircleIcon,
    error: XCircleIcon,
    warning: ExclamationTriangleIcon,
    info: InformationCircleIcon,
};

const notificationColors = {
    success: 'text-green-400',
    error: 'text-red-400',
    warning: 'text-yellow-400',
    info: 'text-blue-400',
};

export default function GlobalNotifications() {
    const dispatch = useAppDispatch();
    const notifications = useAppSelector(
        (state) => state.notifications.notifications
    );

    return (
        <div
            aria-live="assertive"
            className="pointer-events-none fixed inset-0 z-50 flex items-end px-4 py-6 sm:items-start sm:p-6"
        >
            <div className="flex w-full flex-col items-center space-y-4 sm:items-end">
                {notifications.map((notification) => (
                    <NotificationItem
                        key={notification.id}
                        notification={notification}
                        onDismiss={() => dispatch(removeNotification(notification.id))}
                    />
                ))}
            </div>
        </div>
    );
}

function NotificationItem({
                              notification,
                              onDismiss,
                          }: {
    notification: Notification;
    onDismiss: () => void;
}) {
    const { type, title, message } = notification;
    const Icon = notificationIcons[type];
    const colorClass = notificationColors[type];

    // Auto-dismiss after 5 seconds
    useEffect(() => {
        const timer = setTimeout(onDismiss, 5000);
        return () => clearTimeout(timer);
    }, [onDismiss]);

    return (
        <Transition
            appear
            show={true}
            as={Fragment}
            enter="transform ease-out duration-300 transition"
            enterFrom="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
            enterTo="translate-y-0 opacity-100 sm:translate-x-0"
            leave="transition ease-in duration-100"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
        >
            <div className="pointer-events-auto w-full max-w-sm overflow-hidden rounded-lg bg-white shadow-lg ring-opacity-5">
                <div className="p-4">
                    <div className="flex items-start">
                        <div className="shrink-0">
                            <Icon className={`h-6 w-6 ${colorClass}`} aria-hidden="true" />
                        </div>
                        <div className="ml-3 w-0 flex-1 pt-0.5">
                            <p className="text-sm font-medium text-gray-900">{title}</p>
                            {message && (
                                <p className="mt-1 text-sm text-gray-500">{message}</p>
                            )}
                        </div>
                        <div className="ml-4 flex shrink-0">
                            <button
                                type="button"
                                className="inline-flex rounded-md bg-white text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                                onClick={onDismiss}
                            >
                                <span className="sr-only">Close</span>
                                <XMarkIcon className="h-5 w-5" aria-hidden="true" />
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </Transition>
    );
}