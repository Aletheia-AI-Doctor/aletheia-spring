import React from "react";
import If from "~/components/if";
import {useAppDispatch, useAppSelector} from "~/base/hooks";
import {ExclamationCircleIcon} from "@heroicons/react/16/solid";
import {clearError} from "~/features/errors/errorSlice";

interface InputProps {
    id: string;
    name?: string;
    type?: string;
    required?: boolean;
    autoComplete?: string;
    label?: string;
    value?: string;
    onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    className?: string;
    defaultValue?: string;
    dir?: string;
    checked?: boolean;
}

const defaultProps: InputProps = {
    type: 'text',
    id: 'input-' + Math.random().toString(36).substring(7),
    dir: 'auto',
}

export default function Input(props: InputProps) {
    props = {...defaultProps, ...props};

    const allErrors = useAppSelector(state => state.globalErrors);
    const name = props.name ?? props.id ?? "";
    const errors = allErrors[name] ?? [];
    const dispatch = useAppDispatch();

    function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        props.onChange && props.onChange(event);

        dispatch(clearError(name));
    }

    if (props.type === "hidden") {
        return (
            <input
                defaultValue={props.defaultValue}
                id={props.id}
                name={props.name}
                type={props.type}
                value={props.value}
                onChange={handleChange}
            />
        );
    }

    return (
        <div className={props.className}>
            <If condition={!! props.label}>
                <label htmlFor={props.id} className="block text-sm/6 font-medium text-gray-900">
                    {props.label} {props.required && props.type !== "checkbox" && <span>*</span>}
                </label>
            </If>
            <div className="mt-2 grid grid-cols-1">
                <If condition={props.type === "checkbox"}>
                    <div className="flex h-6 shrink-0 items-center">
                        <div className="group grid size-4 grid-cols-1">
                            <input
                                checked={props.checked}
                                dir={props.dir}
                                defaultChecked={props.defaultValue === undefined ? undefined : !!props.defaultValue}
                                id={props.id}
                                name={props.name}
                                type={props.type}
                                value={props.value}
                                onChange={handleChange}
                                className="col-start-1 row-start-1 appearance-none rounded-sm border border-gray-300 bg-white
                        checked:border-blue-600 checked:bg-blue-600 indeterminate:border-blue-600 indeterminate:bg-blue-600
                         focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600
                         disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto"
                            />
                            <svg
                                fill="none"
                                viewBox="0 0 14 14"
                                className="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-disabled:stroke-gray-950/25"
                            >
                                <path
                                    d="M3 8L6 11L11 3.5"
                                    strokeWidth={2}
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    className="opacity-0 group-has-checked:opacity-100"
                                />
                                <path
                                    d="M3 7H11"
                                    strokeWidth={2}
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    className="opacity-0 group-has-indeterminate:opacity-100"
                                />
                            </svg>
                        </div>
                    </div>
                </If>

                <If condition={props.type !== "checkbox"}>
                    <input
                        defaultValue={props.defaultValue}
                        placeholder={props.placeholder}
                        id={props.id}
                        name={props.name}
                        dir={props.dir}
                        type={props.type}
                        required={props.required}
                        autoComplete={props.autoComplete}
                        value={props.value}
                        onChange={handleChange}
                        className={"col-start-1 row-start-1 block w-full rounded-md bg-white py-1.5" +
                            " text-base outline-1 -outline-offset-1" +
                            " outline-gray-300 focus:outline-2" +
                            " focus:-outline-offset-2 sm:text-sm/6 "
                            + (errors && errors.length > 0 ? "border-red-500 focus:border-red-500 text-red-900 focus:outline-red-600 pr-10 placeholder:text-red-300 pl-3" : "text-gray-900 focus:outline-blue-600 placeholder:text-gray-400 px-3")
                    }
                    />
                </If>

                {errors && errors.length > 0 && (
                    <ExclamationCircleIcon
                        aria-hidden="true"
                        className="pointer-events-none col-start-1 row-start-1 mr-3 size-5 self-center justify-self-end text-red-500 sm:size-4"
                    />
                )}
            </div>
            <If condition={errors.length > 0}>
                <div className="text-sm text-red-600">
                    {errors.map((error, index) => (
                        <p key={index}>{error}</p>
                    ))}
                </div>
            </If>
        </div>
    );
}