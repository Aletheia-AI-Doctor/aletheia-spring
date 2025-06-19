import React from "react";
import If from "~/components/if";
import {useAppDispatch, useAppSelector} from "~/base/hooks";
import {clearError} from "~/features/errors/errorSlice";
import {ExclamationCircleIcon} from "@heroicons/react/16/solid";

interface SelectProps {
    id: string;
    name?: string;
    required?: boolean;
    label?: string;
    value?: string;
    onChange?: (event: React.ChangeEvent<HTMLSelectElement>) => void;
    placeholder?: string;
    className?: string;
    dir?: string;
    defaultValue?: string;
    options: Array<{ value: string; label: string }>;
    disabled?: boolean;
}

const defaultProps: SelectProps = {
    id: 'input-' + Math.random().toString(36).substring(7),
    dir: 'auto',
    options: [],
}

export default function Select(props: SelectProps) {
    props = {...defaultProps, ...props};

    const allErrors = useAppSelector(state => state.globalErrors);
    const name = props.name ?? props.id ?? "";
    const errors = allErrors[name] ?? [];
    const dispatch = useAppDispatch();

    function handleChange(event: React.ChangeEvent<HTMLSelectElement>) {
        props.onChange && props.onChange(event);

        dispatch(clearError(name));
    }

    return (
        <div className={props.className}>
            <If condition={!! props.label}>
                <label htmlFor={props.id} className="block text-sm/6 font-medium text-gray-900">
                    {props.label} {props.required && <span>*</span>}
                </label>
            </If>
            <div className="mt-2 grid grid-cols-1">
                <select
                    disabled={props.disabled}
                    defaultValue={props.defaultValue}
                    id={props.id}
                    name={props.name}
                    dir={props.dir}
                    required={props.required}
                    value={props.value}
                    onChange={handleChange}
                    className={"col-start-1 row-start-1 block w-full rounded-md bg-white py-1.5" +
                        " text-base outline-1 -outline-offset-1" +
                        " outline-gray-300 focus:outline-2" +
                        " focus:-outline-offset-2 sm:text-sm/6 "
                        + (errors && errors.length > 0 ? "border-red-500 focus:border-red-500 text-red-900 focus:outline-red-600 pr-10 placeholder:text-red-300 pl-3" : "text-gray-900 focus:outline-blue-600 placeholder:text-gray-400 px-3")}
                >
                    {props.placeholder && <option value="">{props.placeholder}</option>}

                    {props.options.map(({label, value}) => (
                        <option key={value} value={value}>{label}</option>
                    ))}
                </select>

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