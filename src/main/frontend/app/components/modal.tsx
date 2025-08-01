import React from 'react'
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from '@headlessui/react'

interface ModalProps {
    onClose: () => void;
    open: boolean;
    children?: React.ReactNode;
    title?: string;
    footer?: React.ReactNode;
    size?: "sm" | "md" | "lg" | "xl";
}

export default function Modal({open, onClose, children, title, footer, size} : ModalProps) {
    const sizeClass = {
        sm: "w-full sm:max-w-lg",
        md: "w-full sm:max-w-2xl",
        lg: "w-full sm:max-w-3xl",
        xl: "w-full sm:max-w-4xl",
    }[size ?? "sm"];

    return (
        <Dialog open={open} onClose={onClose} className="relative z-[50]">
            <DialogBackdrop
                transition
                className="fixed inset-0 bg-gray-500/75 transition-opacity data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in"
            />

            <div className="fixed inset-0 w-screen overflow-y-auto">
                <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                    <DialogPanel
                        transition
                        className={"relative transform overflow-hidden rounded-lg bg-white px-4 pt-5 pb-4 text-left shadow-xl transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in sm:my-8 sm:p-6 data-closed:sm:translate-y-0 data-closed:sm:scale-95 " + sizeClass}
                    >
                        <div>
                            <div className="mt-3 sm:mt-5">
                                {title && (
                                    <DialogTitle as="h3" className="text-base font-semibold text-gray-900">
                                        {title}
                                    </DialogTitle>
                                )}
                                <div className="mt-2">
                                    {children}
                                </div>
                            </div>
                        </div>
                        <div className="mt-5 sm:mt-6 sm:grid sm:grid-flow-row-dense sm:grid-cols-2 sm:gap-3">
                            {footer}
                        </div>
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
}