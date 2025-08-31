import React from "react";
import { useAppSelector, useAppDispatch } from "~/store/hooks";
import {
  searchPubmedArticles,
  searchPubmedArticlesByIds,
  fetchArticleAbstract,
  setCurrentArticle,
} from "~/store/slices/pubmedSlice";
import { addNotification } from "~/store/slices/appSlice";
import { Button } from "~/components/ui/button";
import PubmedArticleList from "~/components/pubmed/PubmedArticleList";
import CurrentPubmedArticle from "~/components/pubmed/CurrentPubmedArticle";
import SearchPubmedArticleForm from "~/components/pubmed/SearchPubmedArticleForm";
import SearchPubmedArticlesForm from "~/components/pubmed/SearchPubmedArticlesForm";

const PubmedSearch: React.FC = () => {
  const dispatch = useAppDispatch();
  const { error } = useAppSelector((state) => state.pubmed);

  return (
    <>
      <h1 className="text-4xl mb-4">PubMed Search</h1>
      <div className="flex flex-row">
        <SearchPubmedArticlesForm />
        <SearchPubmedArticleForm />
      </div>
      {error && <div className="error-message">{error}</div>}

      <CurrentPubmedArticle />
      <PubmedArticleList />
    </>
  );
};

export default PubmedSearch;
