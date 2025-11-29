import { ArrowLeft, Heart } from 'lucide-react';
import { MatchProfileCard } from './MatchProfileCard';

interface Match {
  id: string;
  name: string;
  age: number;
  image: string;
  location: string;
  isNew?: boolean;
}

interface MatchScreenProps {
  matches: Match[];
  onBack: () => void;
  onMessage: (matchId: string) => void;
  onViewProfile: (match: Match) => void;
}

export function MatchScreen({ matches, onBack, onMessage, onViewProfile }: MatchScreenProps) {
  return (
    <div className="h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <div className="bg-white px-6 py-4 border-b border-gray-200">
        <div className="flex items-center gap-4">
          <button
            onClick={onBack}
            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div className="flex-1">
            <div className="flex items-center gap-2">
              <Heart className="w-6 h-6 text-pink-500 fill-pink-500" />
              <span>Matches</span>
            </div>
            <p className="text-sm text-gray-500 mt-1">
              {matches.length} {matches.length === 1 ? 'match' : 'matches'}
            </p>
          </div>
        </div>
      </div>

      {/* Matches Grid */}
      <div className="flex-1 overflow-y-auto p-4">
        <div className="grid grid-cols-2 gap-4 max-w-4xl mx-auto">
          {matches.map((match) => (
            <MatchProfileCard
              key={match.id}
              id={match.id}
              name={match.name}
              age={match.age}
              image={match.image}
              location={match.location}
              isNew={match.isNew}
              onMessage={() => onMessage(match.id)}
              onViewProfile={() => onViewProfile(match)}
            />
          ))}
        </div>
      </div>
    </div>
  );
}