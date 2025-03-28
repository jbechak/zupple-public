import ServiceBase from "@/services/ServiceBase";

export default class WordsearchService extends ServiceBase {
  static get serviceRoot() {
    return "wordsearch";
  }
}