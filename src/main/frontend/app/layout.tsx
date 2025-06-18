import {useEffect, useLayoutEffect, useState} from 'react'
import {
    Dialog,
    DialogBackdrop,
    DialogPanel,
    Menu,
    MenuButton,
    MenuItem,
    MenuItems,
    TransitionChild,
     Popover,
     PopoverButton,
     PopoverPanel

} from '@headlessui/react'
import {
    Bars3Icon,
    BellIcon,
    DocumentMagnifyingGlassIcon,
    HomeIcon,
    XMarkIcon,
    UsersIcon,
    ChatBubbleBottomCenterIcon
} from '@heroicons/react/24/outline'
import { ChevronDownIcon } from '@heroicons/react/20/solid'
import {Link, Outlet, useNavigate} from "react-router";
import AppLogoIcon from "~/components/app-logo-icon";
import {useAppDispatch, useAppSelector} from "~/base/hooks";
import {clearAuth, setDoctor} from "~/features/authentication/authenticationApiSlice";
import {useGetDoctorAttributesQuery} from "~/features/doctor/doctorApiSlice";
import GlobalNotifications from "~/components/notification";
import {useGetNotificationsQuery} from "~/features/community/notficationApiSlice";

const userNavigation = [
    { name: 'Your profile', href: '/profile' },
]

function classNames(...classes : string[]) {
    return classes.filter(Boolean).join(' ')
}

function PrivateRoute () {
    const navigate = useNavigate();
    const token = useAppSelector((state) => state.auth.token);

    useEffect(() => {
        if (! token) {
            navigate("/login", { replace: true });
        }
    }, [token, navigate]);

    return token ? <Outlet/> : null;
}



export default function Layout() {
    const [sidebarOpen, setSidebarOpen] = useState(false)
    const stripHtmlTags = (html: string): string => {
        return html?.replace(/<[^>]*>/g, '').trim() || '';
    };

    const [navigation, setNavigation] = useState([
        { name: 'Dashboard', href: '/', icon: HomeIcon, current: true },
        { name: 'Diagnose', href: '/diagnose', icon: DocumentMagnifyingGlassIcon, current: false },
        { name: 'Patients', href: '/patients', icon: UsersIcon, current: false },
        { name: 'Doxa', href: '/doxa', icon: ChatBubbleBottomCenterIcon, current: false },
    ]);

    const dispatch = useAppDispatch();
    const { refetch } = useGetDoctorAttributesQuery();

    useEffect(() => {
        async function loadDoctor() {
            const { data: doctorData } = await refetch();
            if (doctorData) {
                dispatch(setDoctor(doctorData));
            }
        }

        loadDoctor();
    }, [dispatch, refetch]);

    const doctor = useAppSelector((state) => state.auth.doctor);

    function handleLogout() {
        dispatch(clearAuth());
    }

    useLayoutEffect(() => {
        setNavigation(navigation.map((item) => {
            item.current = window.location.pathname === item.href;
            return item;
        }));
    }, [window.location.pathname]);
    const { data: notifications, isLoading, isError } = useGetNotificationsQuery();

    return (
        <>
            <div>
                <Dialog open={sidebarOpen} onClose={setSidebarOpen} className="relative z-50 lg:hidden">
                    <DialogBackdrop
                        transition
                        className="fixed inset-0 bg-gray-900/80 transition-opacity duration-300 ease-linear data-closed:opacity-0"
                    />

                    <div className="fixed inset-0 flex">
                        <DialogPanel
                            transition
                            className="relative mr-16 flex w-full max-w-xs flex-1 transform transition duration-300 ease-in-out data-closed:-translate-x-full"
                        >
                            <TransitionChild>
                                <div className="absolute top-0 left-full flex w-16 justify-center pt-5 duration-300 ease-in-out data-closed:opacity-0">
                                    <button type="button" onClick={() => setSidebarOpen(false)} className="-m-2.5 p-2.5">
                                        <span className="sr-only">Close sidebar</span>
                                        <XMarkIcon aria-hidden="true" className="size-6 text-white" />
                                    </button>
                                </div>
                            </TransitionChild>
                            {/* Sidebar component, swap this element with another sidebar if you like */}
                            <div className="flex grow flex-col gap-y-5 overflow-y-auto bg-gray-900 px-6 pb-4 ring-1 ring-white/10">
                                <div className="flex h-16 shrink-0 items-center">
                                    <AppLogoIcon className="h-8 w-auto" />
                                </div>
                                <nav className="flex flex-1 flex-col">
                                    <ul role="list" className="flex flex-1 flex-col gap-y-7">
                                        <li>
                                            <ul role="list" className="-mx-2 space-y-1">
                                                {navigation.map((item) => (
                                                    <li key={item.name}>
                                                        <Link
                                                            to={item.href}

                                                            className={classNames(
                                                                item.current
                                                                    ? 'bg-gray-800 text-white'
                                                                    : 'text-gray-400 hover:bg-gray-800 hover:text-white',
                                                                'group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold',
                                                            )}
                                                        >
                                                            <item.icon aria-hidden="true" className="size-6 shrink-0" />
                                                            {item.name}
                                                        </Link>
                                                    </li>
                                                ))}
                                            </ul>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </DialogPanel>
                    </div>
                </Dialog>

                {/* Static sidebar for desktop */}
                <div className="hidden lg:fixed lg:inset-y-0 lg:z-50 lg:flex lg:w-72 lg:flex-col">
                    {/* Sidebar component, swap this element with another sidebar if you like */}
                    <div className="flex grow flex-col gap-y-5 overflow-y-auto bg-gray-900 px-6 pb-4">
                        <div className="flex h-16 shrink-0 items-center">
                            <AppLogoIcon className="h-8 w-auto" />
                        </div>
                        <nav className="flex flex-1 flex-col">
                            <ul role="list" className="flex flex-1 flex-col gap-y-7">
                                <li>
                                    <ul role="list" className="-mx-2 space-y-1">
                                        {navigation.map((item) => (
                                            <li key={item.name}>
                                                <Link
                                                    to={item.href}
                                                    className={classNames(
                                                        item.current
                                                            ? 'bg-gray-800 text-white'
                                                            : 'text-gray-400 hover:bg-gray-800 hover:text-white',
                                                        'group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold',
                                                    )}
                                                >
                                                    <item.icon aria-hidden="true" className="size-6 shrink-0" />
                                                    {item.name}
                                                </Link>
                                            </li>
                                        ))}
                                    </ul>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

                <div className="lg:pl-72">
                    <div className="sticky top-0 z-40 flex h-16 shrink-0 items-center gap-x-4 border-b border-gray-200 bg-white px-4 shadow-xs sm:gap-x-6 sm:px-6 lg:px-8">
                        <button type="button" onClick={() => setSidebarOpen(true)} className="-m-2.5 p-2.5 text-gray-700 lg:hidden">
                            <span className="sr-only">Open sidebar</span>
                            <Bars3Icon aria-hidden="true" className="size-6" />
                        </button>

                        {/* Separator */}
                        <div aria-hidden="true" className="h-6 w-px bg-gray-900/10 lg:hidden" />

                        <div className="flex flex-1 gap-x-4 self-stretch lg:gap-x-6">
                            <div className="grid flex-1 grid-cols-1" />
                            <div className="flex items-center gap-x-4 lg:gap-x-6">
                                <Popover className="relative">
    <PopoverButton className="-m-2.5 p-2.5 text-gray-400 hover:text-gray-500 relative">
        <span className="sr-only">View notifications</span>
        <BellIcon aria-hidden="true" className="size-6" />
        {
            notifications && notifications.length > 0 && (
                <span className="absolute top-0 right-0 inline-flex h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white" />
            )
        }
    </PopoverButton>
    
    <PopoverPanel 
        transition
        className="absolute right-0 z-10 mt-2.5 w-80 origin-top-right rounded-md bg-white py-2 shadow-lg ring-1 ring-gray-900/5 focus:outline-none data-closed:opacity-0 data-closed:scale-95"
    >
        <div className="p-2 max-h-96 overflow-y-auto">
            {isLoading ? (
                <div className="text-center text-sm text-gray-500 p-4">
                    Loading notifications...
                </div>
            ) : isError ? (
                <div className="text-center text-sm text-red-500 p-4">
                    Failed to load notifications
                </div>
            ) : (
                // Replace the empty notification sections in your PopoverPanel with this code:

<>
    {/* Votes notification */}
    {notifications && notifications.vote > 0 && (
        <div className="flex items-start space-x-3 p-3 hover:bg-gray-50 rounded-lg border-b border-gray-100">
            <div className="flex-shrink-0">
                <div className="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center">
                    <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 15l7-7 7 7" />
                    </svg>
                </div>
            </div>
            <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-gray-900">
                    New Votes Received
                </p>
                <p className="text-sm text-gray-500">
                    You received {notifications.vote} new vote{notifications.vote !== 1 ? 's' : ''} on your posts
                </p>
                <p className="text-xs text-gray-400 mt-1">
                    {new Date(notifications.createdAt).toLocaleDateString()}
                </p>
            </div>
        </div>
    )}

    {/* Replies notifications */}

    {notifications && notifications.replies && notifications.replies.length > 0 && (
    <div className="space-y-2">
        <div className="px-3 py-2 border-b border-gray-100">
            <h4 className="text-sm font-medium text-gray-900">New Replies</h4>
        </div>
        {notifications.replies.map((reply, index) => (
            <div key={reply.id || index} className="flex items-start space-x-3 p-3 hover:bg-gray-50 rounded-lg">
                <div className="flex-shrink-0">
                    <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                        <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                        </svg>
                    </div>
                </div>
                <div className="flex-1 min-w-0">

                    <p className="text-sm font-medium text-gray-900">
                        New reply from {reply.doctor?.name || 'Unknown'}
                    </p>
                    <p className="text-sm text-gray-500 line-clamp-2">
                        {reply.body || ''?.length > 100 ? '...' : ''}
                    </p>
                </div>
            </div>
        ))}
    </div>
)}

    {/* No notifications message */}
    {notifications && notifications.vote === 0 && (!notifications.replies || notifications.replies.length === 0) && (
        <div className="text-center text-sm text-gray-500 p-4">
            No new notifications
        </div>
    )}
</>
            )}
        </div>
        

    </PopoverPanel>
</Popover>

                                {/* Separator */}
                                <div aria-hidden="true" className="hidden lg:block lg:h-6 lg:w-px lg:bg-gray-900/10" />

                                {/* Profile dropdown */}
                                <Menu as="div" className="relative">
                                    <MenuButton className="-m-1.5 flex items-center p-1.5">
                                        <span className="sr-only">Open user menu</span>
                                        <img
                                            // alt=""
                                            src={doctor?.image}
                                            className="size-8 rounded-full bg-gray-50"
                                        />
                                        <span className="hidden lg:flex lg:items-center">
                                          <span aria-hidden="true" className="ml-4 text-sm/6 font-semibold text-gray-900">
                                            {doctor?.name}
                                          </span>
                                          <ChevronDownIcon aria-hidden="true" className="ml-2 size-5 text-gray-400" />
                                        </span>
                                    </MenuButton>
                                    <MenuItems
                                        transition
                                        className="absolute right-0 z-10 mt-2.5 w-32 origin-top-right rounded-md bg-white py-2 ring-1 shadow-lg ring-gray-900/5 transition focus:outline-hidden data-closed:scale-95 data-closed:transform data-closed:opacity-0 data-enter:duration-100 data-enter:ease-out data-leave:duration-75 data-leave:ease-in"
                                    >
                                        {userNavigation.map((item) => (
                                            <MenuItem key={item.name}>
                                                <Link
                                                    to={item.href}
                                                    className="block px-3 py-1 text-sm/6 text-gray-900 data-focus:bg-gray-50 data-focus:outline-hidden"
                                                >
                                                    {item.name}
                                                </Link>
                                            </MenuItem>
                                        ))}
                                        <MenuItem>
                                            <button
                                                type="button"
                                                onClick={handleLogout}
                                                className="block w-full px-3 py-1 text-left text-sm/6 text-gray-900 data-focus:bg-gray-50 data-focus:outline-hidden"
                                            >
                                                Sign out
                                            </button>
                                        </MenuItem>
                                    </MenuItems>
                                </Menu>
                            </div>
                        </div>
                    </div>

                    <main className="py-10">
                        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                            <GlobalNotifications />
                            <PrivateRoute />
                        </div>
                    </main>
                </div>
            </div>
        </>
    )
}
