import { useAppDispatch, useAppSelector } from "~/store/hooks";
import { Button } from "../ui/button";
import React from "react";
import { searchPubmedArticles } from "~/store/slices/pubmedSlice";
import { addNotification } from "~/store/slices/appSlice";
import { Input } from "../ui/input";

const SearchPubmedArticlesForm = () => {
  const dispatch = useAppDispatch();
  const { loading, error } = useAppSelector((state) => state.pubmed);
  const [searchQuery, setSearchQuery] = React.useState("");
  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    try {
      await dispatch(
        searchPubmedArticles({
          query: searchQuery,
          email: "dummy@website.pl",
          maxResults: 20,
        }),
      ).unwrap();
      dispatch(
        addNotification({
          message: "PubMed search completed!",
          type: "success",
        }),
      );
    } catch (error) {
      dispatch(
        addNotification({
          message: "PubMed search failed",
          type: "error",
        }),
      );
    }
  };
  return (
    <div className="mb-2">
      <h2 className="text-2xl mb-2">Search Articles</h2>
      <form
        onSubmit={handleSearch}
        className="shadow rounded px-8 pt-6 pb-8 mb-4"
      >
        <div className="mb-6">
          <Input
            type="text"
            placeholder="Search PubMed articles..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            required
            className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight shadow-outline"
          />
        </div>
        <Button type="submit" disabled={loading} className="w-full">
          {loading ? "Searching..." : "Search"}
        </Button>
      </form>
    </div>
  );
};

export default SearchPubmedArticlesForm;
