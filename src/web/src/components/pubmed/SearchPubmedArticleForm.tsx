import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { Button } from "../ui/button";
import React from "react";
import {
  searchPubmedArticles,
  searchPubmedArticlesByIds,
} from "~/store/slices/pubmedSlice";
import { addNotification } from "~/store/slices/appSlice";
import { Input } from "../ui/input";

const SearchPubmedArticleForm = () => {
  const dispatch = useAppDispatch();
  const { loading } = useAppSelector((state) => state.pubmed);
  const [searchIds, setSearchIds] = React.useState("");
  const handleSearchByIds = async (e: React.FormEvent) => {
    e.preventDefault();
    const ids = searchIds
      .split(",")
      .map((id) => id.trim())
      .filter((id) => id);
    if (ids.length === 0) return;

    try {
      await dispatch(searchPubmedArticlesByIds(ids)).unwrap();
      dispatch(
        addNotification({
          message: "Articles fetched by IDs!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "Failed to fetch articles by IDs",
          type: "error",
        }),
      );
    }
  };
  return (
    <div className="mb-4">
      <h2 className="text-2xl">Search by Article IDs</h2>
      <form
        onSubmit={handleSearchByIds}
        className="shadow rounded px-8 pt-6 pb-8 mb-4"
      >
        <div className="mb-6">
          <Input
            type="text"
            placeholder="Comma-separated article IDs..."
            value={searchIds}
            onChange={(e) => setSearchIds(e.target.value)}
            required
            className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
          />
        </div>
        <Button type="submit" disabled={loading} className="w-full">
          {loading ? "Fetching..." : "Fetch by IDs"}
        </Button>
      </form>
    </div>
  );
};

export default SearchPubmedArticleForm;
