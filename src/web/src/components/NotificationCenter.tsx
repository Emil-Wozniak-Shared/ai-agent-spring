import { useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useAppDispatch, useAppSelector } from "~/store/hooks";
import {
  clearNotifications,
  removeNotification,
} from "~/store/slices/appSlice";

export default function NotificationCenter() {
  const dispatch = useAppDispatch();
  const { notifications } = useAppSelector((state) => state.app);

  useEffect(() => {
    notifications.forEach((notification) => {
      toast(notification.message, { type: notification.type });
      dispatch(removeNotification(notification.id));
    });
  }, [notifications]);
  return <ToastContainer />;
}
