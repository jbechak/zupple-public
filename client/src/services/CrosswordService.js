import ServiceBase from "@/services/ServiceBase";

export default class CrosswordService extends ServiceBase {
  static get serviceRoot() {
    return "crossword";
  }
}