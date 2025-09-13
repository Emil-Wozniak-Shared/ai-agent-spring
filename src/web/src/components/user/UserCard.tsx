import React from "react";
import type { User } from "~/store/slices/userSlice";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import { Button } from "../ui/button";
import { Label } from "@radix-ui/react-label";
import { Input } from "../ui/input";
import { Badge } from "../ui/badge";

interface UserCardProps {
  user: User;
  isEditing: boolean;
  onEdit: () => void;
  onSave: (userData: User) => void;
  onCancel: () => void;
}

const UserCard: React.FC<UserCardProps> = ({
  user,
  isEditing,
  onEdit,
  onSave,
  onCancel,
}) => {
  const [editData, setEditData] = React.useState({
    ...user,
    name: user.name,
    email: user.email,
  });

  const handleSave = () => {
    onSave(editData);
  };

  if (isEditing) {
    return (
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>Edit user</CardTitle>
        </CardHeader>
        <CardContent>
          <form>
            <div className="flex flex-col gap-6">
              <div className="grid gap-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  value={editData.email}
                  onChange={(e) =>
                    setEditData({ ...editData, email: e.target.value })
                  }
                  placeholder="m@example.com"
                  required
                />
              </div>
              <div className="grid gap-2">
                <div className="flex items-center">
                  <Label htmlFor="password">Password</Label>
                  <a
                    href="#"
                    className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                  >
                    Forgot your password?
                  </a>
                </div>
                <Input id="password" type="password" required />
              </div>
            </div>
          </form>
        </CardContent>
        <CardFooter className="flex-col gap-2">
          <Button type="submit" className="w-full" onClick={handleSave}>
            Submit
          </Button>
          <Button type="submit" className="w-full" onClick={onCancel}>
            Cancel
          </Button>
        </CardFooter>
      </Card>
    );
  }

  return (
    <Card className="w-full max-w-sm">
      <CardHeader>
        <CardTitle>
          <>{user.name}</>
        </CardTitle>
        <CardAction>
          <Button className="w-full" onClick={onEdit}>
            Edit
          </Button>
        </CardAction>
      </CardHeader>
      <CardContent>
        <p>First name: {user.firstName}</p>
        <p>Last name: {user.lastName}</p>
        <p>Email: '{user.email}'</p>
        <p>
          Active:{" "}
          {user.active ? (
            <Badge
              variant="secondary"
              className="bg-blue-500 text-white dark:bg-blue-600"
            >
              Yes
            </Badge>
          ) : (
            <Badge
              className="h-5 min-w-5 rounded-full px-1 font-mono tabular-nums"
              variant="destructive"
            >
              No
            </Badge>
          )}
        </p>
      </CardContent>
    </Card>
  );
};

export default UserCard;
